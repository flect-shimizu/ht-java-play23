function UploadService() {
}

UploadService.prototype.upload = function(file) {
	var defer = this.getSignedUrl(file);
	defer.done(function(data) {
		console.log(data);
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
