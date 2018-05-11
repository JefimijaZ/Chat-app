angular.module('chatApp').controller('HomeController',['$scope', '$rootScope', '$http', '$state','UserService', function($scope, $rootScope, $http, $state, UserService){
    var self = this;
    self.credentials = {};
    self.errorMsg = '';
    self.login = login;
    self.logout=logout;
    function login(){
        UserService.send("Login,"+self.credentials.username + "," +self.credentials.password );
    }
    function logout(){
        $rootScope.globals = {};
        $state.go('home-abstract.register');
    }
    
}]);