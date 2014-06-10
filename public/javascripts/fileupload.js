function UploadService() {
}

UploadService.prototype.upload = function(file) {
	this.getSignedUrl(file)
	.done(function(data) {
		console.log(decodeURIComponent(data));
		$.ajax({
			url: decodeURIComponent(data),
			type: 'PUT',
			processData: false,
			contentType: false,
			data: file
		}).done(function(msg) {
			console.log("upload finished." + msg);
		});
	});
}

UploadService.prototype.getSignedUrl = function(file) {
	var defer = $.Deferred();
	$.ajax({
		url: "/fileupload/url",
		type: "GET",
		data: { "name": file.name, "type": file.type },
		success: defer.resolve,
		error: defer.reject
	});
	return defer.promise();
}
