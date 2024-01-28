$(document).ready(function() {
    var $loginForm = $('#loginForm');
    var $signupForm = $('#signupForm');

    $('.message a').click(function(e) {
        e.preventDefault();
        $loginForm.toggleClass('active');
        $signupForm.toggleClass('active');
    });
});

$(document).ready(function() {
    $('#signupForm').on('submit', function(e) {
        e.preventDefault();

        // Gather data from the form
        var userData = {
            username: $('#signupForm input[name="username"]').val(),
            email: $('#signupForm input[name="email"]').val(),
            password: $('#signupForm input[name="password"]').val()
        };
        console.log(userData);

        // Send data to Node-RED
        $.ajax({
            type: 'POST',
            url: 'http://localhost:1880/create-account',
            data: JSON.stringify(userData),
            contentType: 'application/json',
            success: function(response) {
                console.log('Account created:', response);
                alert(response.message);
                // Handle success (e.g., showing a success message)
            },
            error: function(error) {
                console.log('Error:', error);
                alert(error.message);
                // Handle error
            }
        });
    });
});
