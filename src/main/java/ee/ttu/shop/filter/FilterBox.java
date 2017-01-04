package ee.ttu.shop.filter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class FilterBox {
	
	private final Map<Filter, VariantList> filters;
	

	public FilterBox(Map<Filter, List<Filter_variant>> attrs,HttpServletRequest request) throws UnsupportedEncodingException {
		filters=new LinkedHashMap<>();
		for(Filter filter:attrs.keySet()){
			VariantList variantList = null;
			if(request.getParameter(filter.getName())!=null)
				variantList = new VariantList(attrs.get(filter), true, request,filter.getName());
			else
				variantList = new VariantList(attrs.get(filter), false, request,filter.getName());
			filters.put(filter, variantList);
		}
	}

	public Map<Filter, VariantList> getFilters() {
		return filters;
	}


	public class VariantList implements Serializable{
		
		
		private static final long serialVersionUID = -2403079441950322553L;

		
		private final List<Variant> variants;
		private final boolean choosed;
		
		public String getUrl(String filter,String fvValue,HttpServletRequest request,boolean choosed) throws UnsupportedEncodingException{
			String queryString = request.getQueryString();
			
			

			if(StringUtils.isEmpty(queryString)){
				if(fvValue.equals("all"))
					return "";
				else
					return filter+"="+fvValue;
			}else{
				queryString=URLDecoder.decode(request.getQueryString(), "UTF-8");
				if(!choosed){
					String href= queryString;
					if(fvValue.equals("all"))
						href= href;
					else
						href+="&"+filter+"="+fvValue;
					return href;
				}else{
					String v =request.getParameter(filter);
					if(fvValue.equals("all")){
						if(queryString.contains(filter+"="+v+"&"))
							return queryString.replace(filter+"="+v+"&", "");
						else if(queryString.contains("&"+filter+"="+v))
							return queryString.replace("&"+filter+"="+v, "");
						else
							return queryString.replace(filter+"="+v, "");
					}
					else{
						if(queryString.contains("&"+filter+"="+v))
							return queryString.replace("&"+filter+"="+v, "&"+filter+"="+fvValue);
						else
							return queryString.replace(filter+"="+v, filter+"="+fvValue);
					}
				}
			}
		}
		
		public VariantList(List<Filter_variant> filter_variants,boolean choosed,HttpServletRequest request,String filter) throws UnsupportedEncodingException{
			this.variants=new ArrayList<>();
			for(int i=0;i<filter_variants.size();i++){
				Filter_variant filter_variant = filter_variants.get(i);
				if(filter_variant.getParam().length()>0){
					if(choosed){
						if(request.getParameter(filter).equals(filter_variant.getParam()))
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getParam(),request,choosed), true));
						else
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getParam(),request,choosed), false));
					}else{
						if(filter_variant.getValue().equals("all")){
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getValue(),request,choosed), true));
						}else
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getParam(),request,choosed), false));
					}
				}
				else{
					if(choosed){
						if(request.getParameter(filter).equals(filter_variant.getValue())){
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getValue(),request,choosed), true));
						}else{
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getValue(),request,choosed), false));
						}
					}else{
						if(filter_variant.getValue().equals("all")){
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getValue(),request,choosed), true));
						}else
							variants.add(new Variant(filter_variants.get(i), getUrl(filter,filter_variant.getValue(),request,choosed), false));
					}
				}	
			}
			this.choosed=choosed;
		}

		public List<Variant> getVariants() {
			return variants;
		}

		public boolean isChosen() {
			return choosed;
		}
	}

	public static class Variant implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4179718662899764802L;
		private final Filter_variant filter_variant;
		private final String url;
		private final boolean current;
		
		
		public Variant(Filter_variant filter_variant,String url, boolean current) {
			this.filter_variant = filter_variant;
			this.url=url;
			this.current = current;
		}

		public String getUrl() {
			return url;
		}


		public Filter_variant getFilter_variant() {
			return filter_variant;
		}

		public boolean isCurrent() {
			return current;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
		    }
		    if (o == null || getClass() != o.getClass()) {
		      return false;
		    }
		
		    Filter_variant filter_variant = (Filter_variant) o;
		
		    return this.filter_variant.getValue().equals(filter_variant.getValue());
		}
		
		@Override
		public int hashCode() {
			return filter_variant.getValue().hashCode();
		}
		
	}

}
