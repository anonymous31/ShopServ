package ee.ttu.shop.catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CatalogDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDateSource(DataSource dataSource) {
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public List<Catalog> getCatalogs() {
		Map<String, Object> params = new HashMap<>();
		return namedParameterJdbcTemplate
				.query("SELECT CP.id pid, CP.title pt, CP.image pi, CP.urlname pu, CC.title ct, CC.id cid, CC.image ci, CC.urlname cu "
						+ "FROM catalog CP " + "LEFT JOIN catalog CC ON CP.id=CC.upper_catalog_fk "
						+ "WHERE (CP.catalog_level=1 AND CC.title IS NULL) OR (CC.title IS NOT NULL ) "
						+ "ORDER BY CP.id", params, new ResultSetExtractor<List<Catalog>>() {

							@Override
							public List<Catalog> extractData(ResultSet rs) throws SQLException, DataAccessException {
								List<Catalog> catalogs = new ArrayList<>();
								Map<Integer, Catalog> catalogsById = new LinkedHashMap<>();
								while (rs.next()) {
									Integer id = rs.getInt("pid");
									Catalog catalog = catalogsById.get(id);
									if (catalog == null) {
										catalog = new Catalog();
										catalog.setId(id);
										catalog.setTitle(rs.getString("pt"));
										catalog.setImage(rs.getString("pi"));
										catalog.setUrlname(rs.getString("pu"));

										catalogsById.put(id, catalog);
										catalogs.add(catalog);
									}
									Integer cid = rs.getInt("cid");
									Catalog child = catalogsById.get(cid);
									if (child == null && cid != 0) {
										child = new Catalog();
										child.setId(cid);
										child.setTitle(rs.getString("ct"));
										child.setImage(rs.getString("ci"));
										child.setUrlname(rs.getString("cu"));

										catalog.getChilds().add(child);
										catalogsById.put(cid, child);
									}
								}
								return catalogs;
							}

						});
	}
}
