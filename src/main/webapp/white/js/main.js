function getContextPath() {
	return window.location.pathname.substring(0, window.location.pathname
			.indexOf("/", 2));
}

$(document).ready(
		function() {
			$('.dropdown').on(
					'show.bs.dropdown',
					function(e) {
						$(this).find('.dropdown-menu').first().stop(true, true)
								.slideDown();
					});

			$('.dropdown').on(
					'hide.bs.dropdown',
					function(e) {
						$(this).find('.dropdown-menu').first().stop(true, true)
								.slideUp(300, function() {
									$(this).parent().removeClass('open');
								});
					});
		});

var cart = {
	'add' : function(product_id) {
		var quantity = $('#to_basket\\[' + product_id + '\\]').val();
		$
				.ajax({
					url : getContextPath() + '/checkout/cart/add',
					type : 'post',
					data : 'product_id='
							+ product_id
							+ '&quantity='
							+ (typeof (quantity) != 'undefined' && quantity > 0 ? quantity
									: 1),
					dataType : 'json',
					success : function(json) {
						$('#totalItms').html(json.totalItems);
						$('#totalPrice').html(json.totalPrice);
					}
				});
	},
	'update' : function(product_id) {
		var quantity = $('#to_basket\\[' + product_id + '\\]').val();
		$
				.ajax({
					url : getContextPath() + '/checkout/cart/update',
					type : 'post',
					data : 'product_id='
							+ product_id
							+ '&quantity='
							+ (typeof (quantity) != 'undefined' && quantity > 0 ? quantity
									: 1),
					dataType : 'json',
					success : function(json) {

						$('#totalItms').html(json["totalItems"]);
						$('#totalPrice').html(
								parseFloat(json["totalPrice"]).toFixed(2));
						$('#totalPriceDtl').html(
								parseFloat(json["totalPrice"]).toFixed(2));
						$('#rowPrice\\[' + product_id + '\\]').html(
								parseFloat(json["rowPrice"]).toFixed(2));
					}
				});
	}
}