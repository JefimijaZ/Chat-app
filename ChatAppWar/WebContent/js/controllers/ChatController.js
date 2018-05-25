angular.module('chatApp').controller('ChatController', ['$scope', '$websocket', '$rootScope', '$state', '$stateParams',
    function ($scope, $websocket, $rootScope, $state, $stateParams) {
        console.log($stateParams.friendUsername + " ovo je state params");
        var self = this;
        self.conversationList = {};
        self.conversations = [];
        self.messages = [];
        var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");
        ws.send("getConversations," + $rootScope.globals.currentUser.username);

        function send(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
        ws.onMessage(function (event) {
            console.log('message from chat: ', event);
            var command = event.data.split("/")[0];
            if (command === "conversations") {
                self.conversations = [];
                self.conversationList = JSON.parse(event.data.split("/")[1]);
                for (var i = 0; i < self.conversationList.length; i++) {
                    if (self.conversationList[i].user.username === $stateParams.friendUsername) {
                        self.messages.push(self.conversationList[i]);
                    } else if (self.conversationList[i].friend.username === $stateParams.friendUsername) {
                        self.messages.push(self.conversationList[i]);
                    }
                }
                console.log(self.messages);
            } else if (command === "friends") {
                self.friendsList = JSON.parse(event.data.split("/")[1]);
                console.log(self.friendsList);

            }
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
            ws.send("Logout," + $rootScope.globals.currentUser.username);
        });


    }
]);