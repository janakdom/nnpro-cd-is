package cz.janakdom.backend.controller;

import cz.janakdom.backend.Creator;
import cz.janakdom.backend.controller.rest.internal.CarriageController;
import cz.janakdom.backend.model.ApiResponse;
import cz.janakdom.backend.model.database.Carriage;
import cz.janakdom.backend.model.dto.carriage.CarriageDto;
import cz.janakdom.backend.model.dto.carriage.CarriageUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CarriageIntegrationTest {

    @Autowired
    private Creator creator;

    @Autowired
    private CarriageController carriageController;

    @Test
    public void crud() {
        verifyCrud(1, null, Arrays.asList()); // Empty DB.

        Carriage carriage1 = carriageController.createCarriage(new CarriageDto("A", "B", "C", "D")).getResult();
        Carriage carriage2 = carriageController.createCarriage(new CarriageDto("B", "C", "D", "E")).getResult();

        verifyCrud(carriage1.getId(), carriage1, Arrays.asList(carriage1, carriage2)); // Two added items.

        carriage1 = carriageController.updateCarriage(carriage1.getId(), new CarriageUpdateDto("C", "D", "E")).getResult();

        verifyCrud(carriage2.getId(), carriage2, Arrays.asList(carriage1, carriage2)); // Updated item.

        carriageController.deleteCarriage(carriage1.getId());

        verifyCrud(carriage1.getId(), null, Arrays.asList(carriage2)); // Deleted item.
    }

    @Test
    public void deleteNotFound() {
        Carriage carriage = creator.saveEntity(new Carriage());

        ApiResponse<Void> response = carriageController.deleteCarriage(carriage.getId() * 2);
        ApiResponse<Carriage> expected = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "INVALID", null);
        // TODO: Change expected 400 INVALID to 404 NOT-FOUND?
        Assertions.assertThat(response).isEqualTo(expected);

        verifyCrud(carriage.getId(), carriage, Arrays.asList(carriage)); // There should be still ona carriage in DB.
    }

    @Test
    public void updateNotFound() {
        Carriage carriage = creator.saveEntity(new Carriage());

        ApiResponse<Carriage> response = carriageController.updateCarriage(carriage.getId() * 2, new CarriageUpdateDto("A", "B", "C"));
        ApiResponse<Carriage> expected = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "NOT-FOUND", null);

        Assertions.assertThat(response).isEqualTo(expected);

        verifyCrud(carriage.getId(), carriage, Arrays.asList(carriage)); // There should be non-updated carriage in DB.
    }

    @Test
    public void create() {
        CarriageDto carriageDto1 = new CarriageDto(null, "B", "C", "D");
        CarriageDto carriageDto2 = new CarriageDto("B", null, "D", "E");
        CarriageDto carriageDto3 = new CarriageDto("C", "D", "E", null);
        CarriageDto carriageDto4 = new CarriageDto("D", "E", null, "G");
        CarriageDto carriageDto5 = new CarriageDto("D", "F", "G", "H");

        ApiResponse<Carriage> response1 = carriageController.createCarriage(carriageDto1);
        ApiResponse<Carriage> response2 = carriageController.createCarriage(carriageDto2);
        ApiResponse<Carriage> response3 = carriageController.createCarriage(carriageDto3);
        ApiResponse<Carriage> response4 = carriageController.createCarriage(carriageDto4);
        ApiResponse<Carriage> response5 = carriageController.createCarriage(carriageDto5);

        ApiResponse<Carriage> expected1 = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "EMPTY-SERIAL-NUMBER", null);
        ApiResponse<Carriage> expected2 = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "EMPTY-PRODUCER", null);
        ApiResponse<Carriage> expected3 = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "EMPTY-HOME-STATION", null);
        ApiResponse<Carriage> expected4 = new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", response4.getResult());
        ApiResponse<Carriage> expected5 = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "ALREADY-EXISTS", null);

        Assertions.assertThat(response1).isEqualTo(expected1);
        Assertions.assertThat(response2).isEqualTo(expected2);
        Assertions.assertThat(response3).isEqualTo(expected3);
        Assertions.assertThat(response4).isEqualTo(expected4);
        Assertions.assertThat(response5).isEqualTo(expected5);

        Carriage carriage = response4.getResult();
        verifyCrud(carriage.getId(), carriage, Arrays.asList(carriage)); // There should be single carriage in DB.
    }

    @Test
    public void update() {
        Carriage carriage1 = creator.saveEntity(new Carriage(1, "A"));
        CarriageUpdateDto updated = new CarriageUpdateDto(null, carriage1.getColor(), carriage1.getHomeStation());

        ApiResponse<Carriage> response = carriageController.updateCarriage(carriage1.getId(), updated);
        ApiResponse<Carriage> expected = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "EMPTY-PRODUCER", null);

        Assertions.assertThat(response).isEqualTo(expected); // Update should not be performed because of null producer.

        updated.setProducer(carriage1.getProducer());
        updated.setHomeStation("");

        response = carriageController.updateCarriage(carriage1.getId(), updated);
        expected = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "EMPTY-HOME-STATION", null);

        Assertions.assertThat(response).isEqualTo(expected); // Update should not be performed because empty home station.

        updated.setHomeStation(carriage1.getHomeStation());
        updated.setColor(null);
        carriage1.setColor(null);

        response = carriageController.updateCarriage(carriage1.getId(), updated);
        expected = new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", carriage1);

        Assertions.assertThat(response).isEqualTo(expected); // Update should be performed.

        verifyCrud(carriage1.getId(), carriage1, Arrays.asList(carriage1));
    }

    /**
     * Test findCarriage, listCarriages and findCarriageSerialNumber methods for given carriages.
     */
    private void verifyCrud(int findById, Carriage expected, List<Carriage> expectedAll) {
        ApiResponse<Carriage> response = carriageController.findCarriage(findById);
        ApiResponse<Page<Carriage>> responseAll = carriageController.listCarriages(null);

        int expectedCode = expected == null ? 404 : 200;
        String expectedStatus = expected == null ? "NOT-FOUND" : "SUCCESS";
        ApiResponse<Carriage> expectedResponse = new ApiResponse<>(expectedCode, expectedStatus, expected);
        ApiResponse<Page<Carriage>> expectedAllResponse = new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", new PageImpl<>(expectedAll));

        Assertions.assertThat(response).isEqualTo(expectedResponse);
        Assertions.assertThat(responseAll).isEqualTo(expectedAllResponse);

        if (expected != null) {
            ApiResponse<Carriage> responseSerialNumber = carriageController.findCarriageSerialNumber(expected.getSerialNumber());
            ApiResponse<Carriage> expectedSerialNumber = new ApiResponse<>(HttpStatus.OK.value(), "SUCCESS", expected);

            Assertions.assertThat(responseSerialNumber).isEqualTo(expectedSerialNumber);
        }
    }

}
