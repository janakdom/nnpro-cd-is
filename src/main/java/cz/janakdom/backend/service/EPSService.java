package cz.janakdom.backend.service;

import cz.janakdom.backend.dao.EPSDao;
import cz.janakdom.backend.model.database.Building;
import cz.janakdom.backend.model.database.EPS;
import cz.janakdom.backend.model.database.FireExtinguisher;
import cz.janakdom.backend.model.dto.EPSDto;
import cz.janakdom.backend.model.dto.TechnicalSystemDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "epsService")
public class EPSService {

    @Autowired
    private EPSDao epsDao;
    @Autowired
    private BuildingService buildingService;

    public List<EPS> findAll() {
        return epsDao.findAll();
    }

    public EPS findById(Integer id) {
        Optional<EPS> eps = epsDao.findById(id);
        return eps.orElse(null);
    }

    public EPS save(EPSDto inputModel) {
        EPS eps = new EPS();
        BeanUtils.copyProperties(inputModel, eps, "id");
        Building building = buildingService.findById(inputModel.getBuilding_id());
        if (building != null){
            eps.setBuilding(building);
            return epsDao.save(eps);
        } else{
            return null;
        }
    }

    public void deleteAllByBuildingId(Integer buildingId){
        epsDao.deleteAllByBuildingId(buildingId);
    }

    public boolean delete(Integer id) {
        EPS eps = this.findById(id);
        epsDao.delete(eps);
        return true;
    }
}
