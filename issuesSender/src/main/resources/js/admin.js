(function ($) {
 
    var url = AJS.contextPath() + "/rest/issuesSender/1.0/";
 
    $(document).ready(function() {
        $.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) {
            $("#email").val(config.email);
            $("#time").val(config.time);
        });
    });

})(AJS.$ || jQuery);