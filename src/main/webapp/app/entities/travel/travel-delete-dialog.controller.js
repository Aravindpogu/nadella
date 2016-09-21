(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('TravelDeleteController',TravelDeleteController);

    TravelDeleteController.$inject = ['$uibModalInstance', 'entity', 'Travel'];

    function TravelDeleteController($uibModalInstance, entity, Travel) {
        var vm = this;

        vm.travel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Travel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
