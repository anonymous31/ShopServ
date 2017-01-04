package ee.ttu.shop.product;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class ProductListDto {

	private int offset = 0;
	private Integer lmt = null;
	private Integer catalogId = null;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	public Integer getLmt() {
		return lmt;
	}

	public void setLmt(Integer lmt) {
		this.lmt = lmt;
	}
}
