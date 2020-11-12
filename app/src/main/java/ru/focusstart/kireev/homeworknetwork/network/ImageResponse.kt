package ru.focusstart.kireev.homeworknetwork.network

class ImageResponse {
    var success = false
    var status = 0
    var data: UploadedImage? = null

    class UploadedImage {
        var id: String? = null
        var title: String? = null
        var description: String? = null
        var type: String? = null
        var animated = false
        var width = 0
        var height = 0
        var size = 0
        var views = 0
        var bandwidth = 0
        var vote: String? = null
        var favorite = false
        var account_url: String? = null
        var deletehash: String? = null
        var name: String? = null
        var link: String? = null
        override fun toString(): String {
            return "UploadedImage{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", type='" + type + '\'' +
                    ", animated=" + animated +
                    ", width=" + width +
                    ", height=" + height +
                    ", size=" + size +
                    ", views=" + views +
                    ", bandwidth=" + bandwidth +
                    ", vote='" + vote + '\'' +
                    ", favorite=" + favorite +
                    ", account_url='" + account_url + '\'' +
                    ", deletehash='" + deletehash + '\'' +
                    ", name='" + name + '\'' +
                    ", link='" + link + '\'' +
                    '}'
        }
    }

    override fun toString(): String {
        return "ImageResponse{" +
                "success=" + success +
                ", status=" + status +
                ", data=" + data.toString() +
                '}'
    }
}