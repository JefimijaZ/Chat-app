angular.module('chatApp').controller('HomeController', ['$scope', '$rootScope', '$websocket', '$state', 'UserService', function ($scope, $rootScope, $websocket, $state, UserService) {
    var self = this;
    self.credentials = {};
    self.errorMsg = '';
    self.login = login;
    self.logout = logout;
    self.send = send;
    self.conversationList = {};
    // self.conversationList = conversationList;
    var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
    if($rootScope.globals.currentUser.username){
        ws.send("getConversations," + $rootScope.globals.currentUser.username);
    }
    

    function send(message) {
        if (angular.isString(message)) {
            ws.send(message);
        } else if (angular.isObject(message)) {
            ws.send(JSON.stringify(message));
        }
    }
    ws.onMessage(function (event) {
        console.log('message from search: ', event);
        var command = event.data.split("/")[0];
        if (command === "conversations") {
            self.conversationList = JSON.parse(event.data.split("/")[1]);
        }
    });
    ws.onClose(function (event) {
        console.log('connection closed', event);
        ws.send("Logout," + $rootScope.globals.currentUser.username);
    });

    function login() {
        UserService.send("Login," + self.credentials.username + "," + self.credentials.password);
    }

    function logout() {
        UserService.send("Logout," + $rootScope.globals.currentUser.username);
        $rootScope.globals = {
            currentUser: {
                username: null
            }
        }

        $state.go('home-abstract.register');
    }

}]);