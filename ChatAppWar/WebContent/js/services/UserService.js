'use strict';

angular.module('chatApp').factory('UserService', ['$websocket', '$q', '$rootScope', '$state', 'urls',
    function ($websocket, $q, $rootScope, $state, urls) {
        var ws = $websocket("ws://" + document.location.host + "/ChatAppWar/usersEndPoint");

        ws.onOpen(function () {
            console.log('connection open');
            ws.send('HELLO SERVER');
        });

        ws.onMessage(function (event) {
            console.log('message: ', event.data);
            var command = event.data.split(",")[0];
            switch (command) {
                case "login":
                    if (event.data.split(",")[1] === "true") {
                        $rootScope.globals = {                
                            currentUser: {                    
                                username: event.data.split(",")[2]
                                       
                            }
                        
                        }
                        console.log($rootScope.globals);
                        console.log($rootScope.globals.currentUser.username);
                        $state.go('home-abstract.profile-abstract.profile-overview', {
                            username: $rootScope.globals.currentUser.username
                        });
                    }
                
            }
            var response;

            console.log(event.data);
        });
        ws.onClose(function (event) {
            console.log('connection closed', event);
            ws.send("Logout,"+$rootScope.globals.currentUser.username);
        });


        var factory = {
            send: send,
        };

        return factory;

        function send(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
    }
]);