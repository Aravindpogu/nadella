(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('TravelDetailController', TravelDetailController);

    TravelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Travel', 'Tracking'];

    function TravelDetailController($scope, $rootScope, $stateParams, previousState, entity, Travel, Tracking) {
        var vm = this;

        vm.travel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nadellaApp:travelUpdate', function(event, result) {
            vm.travel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
