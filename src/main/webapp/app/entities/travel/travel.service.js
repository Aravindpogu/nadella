(function() {
    'use strict';
    angular
        .module('nadellaApp')
        .factory('Travel', Travel);

    Travel.$inject = ['$resource', 'DateUtils'];

    function Travel ($resource, DateUtils) {
        var resourceUrl =  'api/travels/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.from_date = DateUtils.convertLocalDateFromServer(data.from_date);
                        data.to_date = DateUtils.convertLocalDateFromServer(data.to_date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.from_date = DateUtils.convertLocalDateToServer(data.from_date);
                    data.to_date = DateUtils.convertLocalDateToServer(data.to_date);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.from_date = DateUtils.convertLocalDateToServer(data.from_date);
                    data.to_date = DateUtils.convertLocalDateToServer(data.to_date);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
