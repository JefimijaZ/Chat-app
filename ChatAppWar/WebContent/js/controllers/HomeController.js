angular.module('chatApp').controller('HomeController', ['$scope', '$rootScope', '$http', '$state', 'UserService', function ($scope, $rootScope, $http, $state, UserService) {
    var self = this;
    self.credentials = {};
    self.errorMsg = '';
    self.login = login;
    self.logout = logout;

    function login() {
        UserService.send("Login," + self.credentials.username + "," + self.credentials.password);
    }

    function logout() {
        UserService.send("Logout,"+$rootScope.globals.currentUser.username);
        $rootScope.globals = {                
            currentUser: {                    
                username: null       
            }
        }
        
        $state.go('home-abstract.register');
    }

}]);