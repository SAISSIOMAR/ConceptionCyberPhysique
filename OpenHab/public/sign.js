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
            url: 'http://'+window.location.hostname +':1880/create-account',
            data: JSON.stringify(userData),
            contentType: 'application/json',
            success: function(response) {
                console.log('Account created:', response);
                //redirect to login page
                    window.location.href = 'http://localhost:3000/';

                // Handle success (e.g., showing a success message)
            },
            error: function(error) {
                console.log('Error:', error);
                alert(error.message);
                // Handle error
            }
        });
    });

    $('#loginForm').on('submit', function(e) {
        e.preventDefault();

        // Gather data from the form
        var userData = {
            username: $('#loginForm input[name="username"]').val(),
            password: $('#loginForm input[name="password"]').val()
        };
        console.log(userData);

        // Send data to Node-RED
        $.ajax({
            type: 'POST',
            url: 'http://'+window.location.hostname +':1880/sign-in',
            data: JSON.stringify(userData),
            contentType: 'application/json',
            success: function(response) {
                console.log('sign in succesfully', response);
                //store the response payload in the local storage
                localStorage.setItem('userData', JSON.stringify(response));
                console.log('token', localStorage.getItem('token'));
                
                window.location.href = 'http://'+window.location.hostname+':3000/home';
                localStorage.setItem('username', userData.username);

                
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
