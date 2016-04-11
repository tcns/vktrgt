/**
 * Created by root on 3/14/16.
 */
angular.module('vktrgtApp')
    .factory('VKService', function ($rootScope, $cookies, $http, $q) {
        var PREFIX="https://api.vk.com/method/";
        return {
            getUsersFromGroup: function(id, from, count) {
                var users = $http.jsonp(PREFIX+"groups.getMembers", {
                    params:{
                        group_id: id,
                        offset: from,
                        count:count,
                        callback : 'JSON_CALLBACK'
                    }
                })
                return users;
            },
            getUserCountsFromGroups: function(groups) {
                var queries = [];
                for (var i in groups) {
                    queries.push(this.getUsersFromGroup(groups[i], 0, 1));
                }
                return $q.all(queries);
            },
            getAllUsersFromGroup: function(id, userCount) {
                var queries = [];
                for (var i = 0; i<userCount; i+=1000) {
                    queries.push(this.getUsersFromGroup(id, i, 1000));
                }
                return $q.all(queries);
            },
            getAllUsersFromGroups: function(groups) {
                var defer = $q.defer();
                var context = this;
                this.getUserCountsFromGroups(groups).then(function(r) {
                    var all = [];
                    for (var i in r) {
                        var resp = r[i];
                        var count = resp.data.response.count;
                        var group = groups[i];
                        all.push(context.getAllUsersFromGroup(group, count));
                    }
                    defer.resolve(all);
                });
                return defer.promise;
            }

        }

    })
