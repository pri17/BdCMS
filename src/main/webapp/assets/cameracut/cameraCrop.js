var cameraCrop = (function () {
    function cameraCrop(selector, width, height) {
        var cpId = "#" + selector;
        this.cameraId = 'camera_' + new Date().getTime();
        this.cameraIdSelector = "#" + this.cameraId;
        this.cropId = 'crop_' + new Date().getTime();
        this.cropIdSelector = "#" + this.cropId;
        $(cpId).html('<div id="' + this.cameraId + '" style="position: absolute;' +
            'width: ' + width + 'px;height: ' + height + 'px;"></div>' +
            '<div id="' + this.cropId + '" style="position: absolute;' +
            'width: ' + width + 'px;height: ' + height + 'px;display: none"></div>');
        Webcam.set({
            width: width,
            height: height,
            dest_width: width,
            dest_height: height,
            crop_width: width,
            crop_height: height,
            image_format: 'jpeg',
            jpeg_quality: 90
        });
    }
    cameraCrop.prototype.start = function () {
        Webcam.attach(this.cameraIdSelector);
    };
    cameraCrop.prototype.switchToCamera = function () {
        $(this.cameraIdSelector).show();
        $(this.cropIdSelector).hide();
    };
    cameraCrop.prototype.take = function (cropRatio) {
        var that = this;
        Webcam.snap(function (data_uri) {
            $(that.cameraIdSelector).hide();
            $(that.cropIdSelector).show();
            that.imgCropId = "cropImg_" + new Date().getTime();
            that.imgCropIdSelector = "#" + that.imgCropId;
            that.imgData = data_uri;
            $(that.cropIdSelector).html('<img id="' + that.imgCropId + '" src="' + data_uri + '"/>');
            if (cropRatio) {
                setTimeout(function () {
                    that.crop(cropRatio);
                }, 200);
            }
        });
    };
    cameraCrop.prototype.crop = function (cropRatio) {
        $(this.imgCropIdSelector).cropper({
            aspectRatio: cropRatio
        });
    };
    cameraCrop.prototype.getImgData = function () {
        var c = $(this.imgCropIdSelector).cropper('getCroppedCanvas');
        return c.toDataURL();
    };
    return cameraCrop;
}());
//# sourceMappingURL=cameraCrop.js.map