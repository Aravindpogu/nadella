'use strict';

describe('Controller Tests', function() {

    describe('Order Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrder, MockItem, MockTracking;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrder = jasmine.createSpy('MockOrder');
            MockItem = jasmine.createSpy('MockItem');
            MockTracking = jasmine.createSpy('MockTracking');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Order': MockOrder,
                'Item': MockItem,
                'Tracking': MockTracking
            };
            createController = function() {
                $injector.get('$controller')("OrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nadellaApp:orderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
