'use strict';

describe('Controller Tests', function() {

    describe('Tracking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTracking, MockOrder, MockTravel;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTracking = jasmine.createSpy('MockTracking');
            MockOrder = jasmine.createSpy('MockOrder');
            MockTravel = jasmine.createSpy('MockTravel');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tracking': MockTracking,
                'Order': MockOrder,
                'Travel': MockTravel
            };
            createController = function() {
                $injector.get('$controller')("TrackingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nadellaApp:trackingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
