var getLocalFavStorage=function(){
    var localFavStorage=$.jStorage.get("localFavStorage");
    if (localFavStorage===null){
        localFavStorage=[];
        $.jStorage.set("localFavStorage", localFavStorage);
    }
    return localFavStorage;
};

var saveLocalFavStorage=function(localFavStorage){
    $.jStorage.set("localFavStorage", localFavStorage);
};

var getFavById=function(advId, favArray){
    var localFav=null;
    $.each(favArray, function(index, object){
        if (object.advId===advId){
            localFav=object;
            return false;
        }
    });
    return localFav;
};

var checkInFav=function(userAuthStatus, advId, callback){
    if (callback!==undefined){
        if (userAuthStatus){
            $.get("/checkInFav?id="+advId, function(data) {
                if(data==='true'){
                    callback(true);
                } else {
                    callback(false);
                }
            });
        } else {
            callback(getFavById(advId, getLocalFavStorage())!==null);
        }
    }
};

var addInFav=function(userAuthStatus, advId, comment){
    if (userAuthStatus){
        $.get("/addInFav?id="+advId+"&comment="+comment);
    } else {
        var localFavStorage=getLocalFavStorage();
        var localFav = getFavById(advId, getLocalFavStorage());
        if (localFav!==null){
            localFav.comment=comment;
        } else {
            localFav={advId: advId, comment: comment};
            localFavStorage.push(localFav);
        }
        saveLocalFavStorage(localFavStorage);
    }
};

var removeInFav=function(userAuthStatus, advId){
    if (userAuthStatus){
        $.get("/removeInFav?id="+advId);
    } else {
        var localFavStorage=getLocalFavStorage();
        var localFav = getFavById(advId, getLocalFavStorage());
        if (localFav!==null){
            localFavStorage.splice($.inArray(localFav, localFavStorage), 1);
            saveLocalFavStorage(localFavStorage);
        }
    }
};

//Return array of advId/comment objects
var listInFav=function(userAuthStatus, callback){
    if (userAuthStatus){
        $.get("/listInFav", function(data) {
          var result=[];
          $.each(data.favList, function(index, object){
            result.push({advId:object.advId, comment:object.comment, advPreview:object.advPreview});
          });
          callback(result);
        });
    } else {
        var localFavStorage = getLocalFavStorage();
        var result=[];
        $.each(localFavStorage, function(index, object){
            result.push({advId:object.advId, comment:object.comment, advPreview:object.advPreview});
        });
        callback(result);
    }
};

