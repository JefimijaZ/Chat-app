angular.module('chatApp').controller('HomeController',['$scope', '$rootScope', '$http', '$state','UserService', function($scope, $rootScope, $http, $state, UserService){
    var self = this;
    var users = UserService.send("allUsers");
    console.log("Ovo je poruka");
    console.log(users);
    self.credentials = {};
    self.errorMsg = '';
    self.login = login;

    function login(){
        UserService.send("Login,"+self.credentials.username + "," +self.credentials.password );
    }
}]);