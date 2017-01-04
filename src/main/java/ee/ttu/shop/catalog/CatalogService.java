package ee.ttu.shop.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

	@Autowired
	CatalogDao catalogDao;

	private List<Catalog> catalogList;

	@PostConstruct
	private void initializeCatalogList() {
		catalogList = catalogDao.getCatalogs();
	}

	public int getIdByName(String name) {
		for (Catalog catalog : catalogList) {
			if (catalog.getUrlname().equals(name)) {
				return catalog.getId();
			}
			for (Catalog child : catalog.getChilds()) {
				if (child.getUrlname().equals(name)) {
					return child.getId();
				}
			}
		}
		throw new CatalogNotFoundException();
	}

	public List<Catalog> getChildsOfSelected(String selected, List<Catalog> catalogs) {
		for (Catalog cata : catalogs) {
			if (cata.getUrlname().equals(selected))
				return cata.getChilds();
		}
		return null;
	}

	public List<Catalog> getCatalogList() {
		if (catalogList.size() == 0) {
			catalogList = catalogDao.getCatalogs();
		}
		return catalogList;
	}

	public void setCatalogList(List<Catalog> catalogList) {
		this.catalogList = catalogList;
	}

}
