function getJson() {
    var obj = {};
    var toJsonObj = $(".toJson");
    $.each(toJsonObj,function (i, o) {
        if(o.type==="radio"){
            if(o.checked===true){
                obj[o.name] = o.value;
            }
        }else if(o.type==="checkbox") {
            if(o.checked===true){
                var oldval = obj[o.name];
                if (typeof(oldval) == "undefined")
                {
                    obj[o.name] = o.value;
                }else {
                    oldval +=","+o.value
                    obj[o.name] = oldval;
                }
            }
        }else{
            obj[o.id] = o.value;
        }
    });
    return obj;
}