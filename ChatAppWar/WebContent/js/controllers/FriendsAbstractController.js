angular.module('chatApp').controller('FriendsAbstractController',['$scope', '$websocket', '$rootScope', '$state',
    function($scope, $websocket, $rootScope, $state){
    
        this.submit = submit;
        this.searchValue="";
        function submit(){
            $state.go("home-abstract.profile-abstract.friends-abstract.friends-search",{searchValue:this.searchValue});
        }

}]);