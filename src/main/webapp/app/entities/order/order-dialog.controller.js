(function() {
    'use strict';

    angular
        .module('nadellaApp')
        .controller('OrderDialogController', OrderDialogController);

    OrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Order', 'Item', 'Tracking'];

    function OrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Order, Item, Tracking) {
        var vm = this;

        vm.order = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.items = Item.query();
        vm.trackings = Tracking.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.order.id !== null) {
                Order.update(vm.order, onSaveSuccess, onSaveError);
            } else {
                Order.save(vm.order, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nadellaApp:orderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.order_date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
