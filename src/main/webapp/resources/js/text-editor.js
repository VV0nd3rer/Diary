$(document).ready(function(){
	tinymce.init({ 
		mode : "exact", 
		elements : "textEditor",
		plugins: [
		          'advlist autolink lists link image charmap print preview hr anchor pagebreak',
		          'searchreplace wordcount visualblocks visualchars code fullscreen',
		          'insertdatetime media nonbreaking save table contextmenu directionality',
		          'emoticons template paste textcolor colorpicker textpattern imagetools codesample toc help'
		        ],
		        toolbar1: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
		        toolbar2: 'print preview media | forecolor backcolor emoticons | codesample help',
		        image_advtab: true,
		        templates: [
		          { title: 'Test template 1', content: 'Test 1' },
		          { title: 'Test template 2', content: 'Test 2' }
		        ]
	
	});
	tinymce.init({
		selector : "#descrEditor",
		menubar: false,
		statusbar: false,
		plugins: [
			'advlist autolink lists link image charmap print preview anchor',
			'searchreplace visualblocks code fullscreen',
			'insertdatetime media table contextmenu paste code'
		],
		toolbar: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image'
	});
});