package ee.ttu.shop.peginator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ee.ttu.shop.catalog.CatalogPrepareService;

public class PagesInfo {
	
	private List<Page> pages;
	
	private int pageLinksSize;

	private boolean hasPrevious;
	private boolean hasFirst;
	private boolean hasCurrent;
	private boolean hasLast;
	private boolean hasNext;

	private String previous;
	private String first;
	private String current;
	private String last;
	private String next;

	public PagesInfo(List<String> pageLinks, int currentPage,int pagesCount,
			String pname, String cname,HttpServletRequest request) throws UnsupportedEncodingException {
		pages = new ArrayList<>();
		pages.add(new Page(CatalogPrepareService.getCatalogLinkPage(pname, cname, 1,request), 0, 0==(currentPage-1) ));
		if(currentPage > 1)
			pages.add(new Page(CatalogPrepareService.getCatalogLinkPage(pname, cname, currentPage,request), currentPage-1, true ));
		if(currentPage>=1 && (currentPage < pagesCount))
			pages.add(new Page(CatalogPrepareService.getCatalogLinkPage(pname, cname, pagesCount,request), pagesCount-1, (pagesCount-1)==(currentPage-1) ));

		pageLinksSize=pagesCount-1;
		hasPrevious = currentPage > 1;
		hasNext = currentPage>=1 && (currentPage < pagesCount );

		if (hasPrevious) {
		  previous = new Page(CatalogPrepareService.getCatalogLinkPage(pname, cname, currentPage-1,request), currentPage-2, false).getUrl();
		} else {
		  previous = null;
		}
		
		
		
		if (hasNext) {
		  next = new Page(CatalogPrepareService.getCatalogLinkPage(pname, cname, currentPage+1,request), currentPage, false).getUrl();
		} else {
		  next = null;
		}
	}


	public static class Page {
		private final String url;
		private final int index;
		private final boolean current;
		
		public Page(String url, int index, boolean current) {
		  this.url = url;
		  this.index = index;
		  this.current = current;
		}
		
		public String getUrl() {
		  return url;
		}
		
		public int getIndex() {
		  return index;
		}
		
		public boolean isCurrent() {
		  return current;
		}
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}


	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}


	public boolean isHasNext() {
		return hasNext;
	}


	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}


	public String getPrevious() {
		return previous;
	}


	public void setPrevious(String previous) {
		this.previous = previous;
	}


	public String getNext() {
		return next;
	}


	public void setNext(String next) {
		this.next = next;
	}


	public List<Page> getPages() {
		return pages;
	}


	public void setPages(List<Page> pages) {
		this.pages = pages;
	}


	public boolean isHasFirst() {
		return hasFirst;
	}


	public void setHasFirst(boolean hasFirst) {
		this.hasFirst = hasFirst;
	}


	public boolean isHasCurrent() {
		return hasCurrent;
	}


	public void setHasCurrent(boolean hasCurrent) {
		this.hasCurrent = hasCurrent;
	}


	public boolean isHasLast() {
		return hasLast;
	}


	public void setHasLast(boolean hasLast) {
		this.hasLast = hasLast;
	}


	public String getFirst() {
		return first;
	}


	public void setFirst(String first) {
		this.first = first;
	}


	public String getCurrent() {
		return current;
	}


	public void setCurrent(String current) {
		this.current = current;
	}


	public String getLast() {
		return last;
	}


	public void setLast(String last) {
		this.last = last;
	}


	public int getPageLinksSize() {
		return pageLinksSize;
	}


	public void setPageLinksSize(int pageLinksSize) {
		this.pageLinksSize = pageLinksSize;
	}

}
