$(document).ready(function() {
	bsCustomFileInput.init()
	console.log($('#fecha'))
	$("#fecha").datepicker({
		dateFormat : "yy-mm-dd"
	});
});

