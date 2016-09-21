(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('TrackingDetailController', TrackingDetailController);

    TrackingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tracking', 'Order', 'Travel'];

    function TrackingDetailController($scope, $rootScope, $stateParams, previousState, entity, Tracking, Order, Travel) {
        var vm = this;

        vm.tracking = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nadellaApp:trackingUpdate', function(event, result) {
            vm.tracking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
