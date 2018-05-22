angular.module('chatApp').controller('FriendsSearchController', ['$scope', '$websocket', '$rootScope', '$state', '$stateParams',
    function ($scope, $websocket, $rootScope, $state, $stateParams) {
        console.log($stateParams.searchValue + "ovo su state params");
        var self = this;
        this.searchValue = $stateParams.searchValue;
        self.searchResults = {};
        self.send = send;
        self.showAdd = showAdd;


        var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
        self.send("Search," + this.searchValue);

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
            if (command === "searchResults") {
                self.searchResults = JSON.parse(event.data.split("/")[1]);
            }
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
            ws.send("Logout," + $rootScope.globals.currentUser.username);
        });
        function showAdd(friendsId){
            for(var i =0; i<self.userData.friends.length; i++){
                if((self.userData.friends[i].friendsKey == friendsId) && self.userData.friends[i].sent === "requested"){
                    return 1;
                }else if((self.userData.friends[i].friendsKey == friendsId) && self.userData.friends[i].sent === "received"){
                    return 2;
                }else if((self.userData.friends[i].friendsKey == friendsId) && self.userData.friends[i].sent === "accepted"){
                    return 3;
                }
            }
            
            return 0;
        }
    }
]);