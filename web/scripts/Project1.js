var Project1 = ( function() {

    return {

        request: function() {
            
            var that = this;
            var ID = $("select").val();
            $.ajax({
                url: 'registration?code=' + ID,
                method: 'GET',
                dataType: 'html',
                success: function(response) {
                    that.output(response);
                }
            });
            
        },
        
        register: function(first, last, display, session) {
            
            var that = this;
            
            var first = $("#first").val();
            var last = $("#last").val();
            var display = $("#display").val();
            var session = $("select").val();
            
            $.ajax({
                url: 'registration?code=' + first + ';' + last + ';' + display + ";" + session,
                method: 'POST',
                dataType: 'json',
                success: function(response) {
                    that.JSONout(response);
                }
            });
            
        },
        
        output: function(response) {
            $("#output").html(response);
        },
        
        JSONout: function(response) {
            var congrats = "Congratulations! You have successfully registered as: ";
            var yourcode = "Your registration code is: ";
            $("#output").html("<p>" + congrats + response["display"] + "<br>" + yourcode + response["registerCode"]);
        },
        
        init: function() {
            
            /* Output the current version of jQuery (for diagnostic purposes) */
            
            $('#output').html( "jQuery Version: " + $().jquery );
 
        }

    };

}());