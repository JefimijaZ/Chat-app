angular.module('chatApp').controller('FriendsListController', ['$scope', '$websocket', '$rootScope', '$http', '$state', 'UserService', function ($scope, $websocket, $rootScope, $http, $state, UserService) {
    var self = this;
    self.send = send;
    self.friendsList={};
    var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
    self.send("getFriends," + $rootScope.globals.currentUser.username);

    function send(message) {
        if (angular.isString(message)) {
            ws.send(message);
        } else if (angular.isObject(message)) {
            ws.send(JSON.stringify(message));
        }
    }
    ws.onMessage(function (event) {
        console.log('message from overview: ', event.data);
        self.friendsList = JSON.parse(event.data);
        
    });
    ws.onClose(function (event) {
        console.log('connection closed', event);
        ws.send("Logout,"+$rootScope.globals.currentUser.username);
    });
}]);