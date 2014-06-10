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

// ファイルのアップロード
function uploadCloudinary(){
        // FormData の作成
        var formData = new FormData();
        var file = $ ( '#upload_file' )[ 0 ].files[ 0 ] ;
    formData.append('picture', file);

        // FormData を送信
        $.ajax('/fileupload/upload', {
            method: 'POST',
            contentType: false,
            processData: false,
            data: formData,
            dataType: 'json',
            error: function(data) {
                console.log('error');
            },
            success: function(data) {
                console.log('success');
                $("#ajaxform").append('<img src="' + data + '">');
            }
        });
};
