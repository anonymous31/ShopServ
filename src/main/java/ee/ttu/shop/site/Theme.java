package ee.ttu.shop.site;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;


public class Theme {
	private static final Theme WHITE = new Theme("white", "white/head-main.jsp");
	
	public static final List<Theme> THEMES = Arrays.asList(WHITE);

	
	private final String id;
	private final String head;

	public Theme(String id, String head) {
		this.id = id;
		this.head = head;
	}

	public String getId() {
		return id;
	}

	public String getHead() {
		return head;
	}
}
