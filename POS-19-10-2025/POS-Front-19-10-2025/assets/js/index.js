// const { $ } = require("protractor");

function GetCanvase(id){
    let canv = document.getElementById(id).fabric;
    console.log('fabric canv',canv)
    return canv;

}
function SetCanvase(id,json){
    let canvas = GetCanvase(id);
    if(canvas)
        canvas.loadFromJSON($.parseJSON(json), canvas.renderAll.bind(canvas));
}
$('.dinein-component').each(function () {
    /* --------------------- start functions --------------------- */



    $(this).find('.colors-list label').each(function () {
        $(this).css('background-color', $(this).data('background'));
    });

    $(this).find('.changeColor').on('click', function () {
        let background = $(this).data('background');
        let color = $(this).data('color');

        if (canvasClicked && !rectSelected) {
            canvas.backgroundColor = background;
            canvas.requestRenderAll();
        } else if (rectSelected) {
            canvas.getActiveObject()._objects[0].set("fill", background);
            canvas.getActiveObject()._objects[1].set("fill", color);
            canvas.requestRenderAll();
        }
    });


    $(this).find('.showHideCtrl').on('click', function () {
        colorsList.removeClass('open');
        if (ctrl.hasClass('ctrl-hide')) {
            ctrl.removeClass('ctrl-hide');
            ctrl.addClass('ctrl-show');
            $(this).find('i').removeClass('fa-pencil-alt').addClass('fa-times');
            canvas.getObjects().forEach(element => {
                element.selectable  = true;
            });
            canvas.requestRenderAll();
        } else if (ctrl.hasClass('ctrl-show') && $(this).find('i').hasClass('fa-times')) {
            ctrl.removeClass('ctrl-show');
            ctrl.addClass('ctrl-hide');
            $(this).find('i').addClass('fa-pencil-alt').removeClass('fa-times');
            /*let cnvs = $(this).closest('.dinein-component').find('canvas');
            cnvs.get(0).toBlob(function (blob) {
                saveAs(blob, 'test.png');
            });*/

            canvas.getObjects().forEach(element => {
                element.selectable  = false;
            });


            canvas.requestRenderAll();
        }
    });

    $(this).find('.ctrl-colors-list').on('click', function () {
        if (colorsList.hasClass('open')) {
            colorsList.removeClass('open');
        } else {
            colorsList.addClass('open');
        }
        console.log('try change color');
    });

    $(this).find('.square-circle').on('click', function () {

        colorsList.removeClass('open');
        if (rectSelected) {
            let rect = canvas.getActiveObject()._objects[0];
            if (rect.rx <= 5) {
                rectRadius = true;
                canvas.getActiveObject()._objects[0].set("rx",rect.width * rect.scaleX);
                canvas.getActiveObject()._objects[0].set("ry",rect.height * rect.scaleY);
                canvas.requestRenderAll();
                sq_cr.removeClass('fa-circle');
                sq_cr.addClass('fa-square');
            } else {
                rectRadius = false;
                canvas.getActiveObject()._objects[0].set("rx",5);
                canvas.getActiveObject()._objects[0].set("ry",5);
                canvas.requestRenderAll();
                sq_cr.removeClass('fa-square');
                sq_cr.addClass('fa-circle');
            }
        }
    });

    $(this).find('.clearCanvas').on('click', function () {
        if (canvas.getActiveObject()) {
            console.log(canvas.getActiveObject());
            svgState.val = canvas.getActiveObject().toSVG();
            canvas.remove(canvas.getActiveObject());
        }
    });

    $(this).find('.restorCanvas').on('click', function () {
        if (svgState.val) {
            fabric.loadSVGFromString(svgState.val, (objects) => {
                let group = new fabric.Group([...objects], {
                    cornerColor: 'gray',
                    cornerSize: 10,
                    transparentCorners: false,
                });
                canvas.add(group);
                objects[1].set('originX', 'center');
                canvas.requestRenderAll();
                svgState.val = null;
            });
        }
    });

    // function setPanEvents(canvas) {
    //     canvas.on('mouse:down', () => {
    //         colorsList.removeClass('open');
    //         canvasClicked = true;
    //         canvas.setCursor('grab');
    //         canvas.renderAll();
    //     });
    // }

    function setPanEvents(canvas) {
        canvas.on('mouse:down', () => {
            colorsList.removeClass('open');
            canvasClicked = true;
            canvas.setCursor('grab');
            canvas.renderAll();
        });
        canvas.on('mouse:dblclick', () => {
            if (canvas._activeObject) {
                let DocumentId = canvas._activeObject.DocumentId;
                // window.location.href = window.location.origin + '/?data=' + DocumentId;
                var url = new URL(window.location.origin+'/order');
                // If your expected result is "http://foo.bar/?x=1&y=2&x=42"
                //url.searchParams.append('data', DocumentId);
                // If your expected result is "http://foo.bar/?x=42&y=2"
                url.searchParams.set('data', DocumentId);
                location.href = url.href.toString();
            }
        });
    }

    $(this).find('.IsActive').on('click', function(){
        // let IsActive = new fabric.Text(rect_Active,{
        //     fontSize: 16,
        //     lineHeight: 16,
        //     height: 16,
        //     fontWeight: "bold",
        //     fontFamily: "Arial",
        //     textAlign: "center",
        //     originX: 'center',
        //     originY: 'center',
        //     fill: 'blue',
        //     left: canvasCenter.left,
        //     top: -15,
        //     id:1
        // });


            if (canvas.getActiveObject()){
                let rect2 = canvas.getActiveObject()._objects[0];
               rect2.set('fill','#ccc')
               rect2.set('rectActive' ,'Active')
            }


    });
    $(this).find('.UnActive').on('click', function(){

            if (canvas.getActiveObject()){
                let rect3 = canvas.getActiveObject()._objects[0];
               rect3.set('fill','white')

            }


    });

    $(this).find('.createRect').on('click', function () {
        colorsList.removeClass('open');
        let canvasCenter = canvas.getCenter();

        let rect = new fabric.Rect({
            width: 100,
            height: 100,
            fill: 'white',
            rx: 5,
            ry: 5,
            borderColor: '#777777',
            objectCaching: false,
            left: canvasCenter.left,
            top: -50,
            originX: 'center',
            originY: 'center'
        });
        let rectText = new fabric.Text(rect_text, {
            fontSize: 16,
            lineHeight: 16,
            height: 16,
            fontWeight: "bold",
            fontFamily: "Arial",
            textAlign: "center",
            originX: 'center',
            originY: 'center',
            fill: 'red',
            left: canvasCenter.left,
            top: -50
        });
        let rectPrice = new fabric.Text(rect_price,{
            fontSize: 16,
            lineHeight: 16,
            height: 16,
            fontWeight: "bold",
            fontFamily: "Arial",
            textAlign: "center",
            originX: 'center',
            originY: 'center',
            fill: 'green',
            left: canvasCenter.left,
            top: -35,
            id:1
        });

        let rectActive = new fabric.Text(rect_active,{
            fontSize: 16,
            lineHeight: 16,
            height: 16,
            fontWeight: "bold",
            fontFamily: "Arial",
            textAlign: "center",
            originX: 'center',
            originY: 'center',
            fill: 'blue',
            left: canvasCenter.left,
            top: -15,
            id:1
        });


        let rectGroup = new fabric.Group([rect,rectText,rectPrice,rectActive],{
            left: canvasCenter.left,
            top: -80,
            originX: 'center',
            originY: 'center',
            cornerColor: 'gray',
            cornerSize: 10,
            transparentCorners: false,
        });

        canvas.add(rectGroup);
        canvas.renderAll();
        rectGroup.animate('top', canvasCenter.top, {
            onChange: canvas.renderAll.bind(canvas)
        });
        rectGroup.on('selected', () => {
            rectSelected = true;
            canvas.requestRenderAll();
            if(rect.rx <= 5) {
                sq_cr.removeClass('fa-square');
                sq_cr.addClass('fa-circle');
            } else {
                sq_cr.addClass('fa-square');
                sq_cr.removeClass('fa-circle');
            }
            // http://localhost:4200/order;data=3
            console.log('location.href')
        });
        rectGroup.on('deselected', () => {
            rectSelected = false;
            canvas.requestRenderAll();
            sq_cr.addClass('fa-square');
            sq_cr.removeClass('fa-circle');
        });
    });

    $(this).find('.openPrompt').on('click', function () {
        if (canvas.getActiveObject()) {
            prompt.addClass('open');
        }
    });

    $(this).find('.update-name').on('click', function () {
        if (nameTable.val()) {
            colorsList.removeClass('open');
            canvas.getActiveObject()._objects[1].set('text', nameTable.val());
            console.log(canvas.getActiveObject()._objects[1].set('text', nameTable.val()).text )
            canvas.requestRenderAll();
            prompt.removeClass('open');
            nameTable.val('');
        } else {
            prompt.removeClass('open');
        }
    });

    $(this).find('.close-prompt').on('click', function () {
        prompt.removeClass('open');
    });

    $(this).find('.duplicateItem').on('click', function () {
        if (canvas.getActiveObject()) {
            canvas.getActiveObject().clone(function(cloned) {
                _clipboard = cloned;
            });
            _clipboard.clone(function(clonedObj) {
                canvas.discardActiveObject();
                clonedObj.set({
                    left: clonedObj.left + 10,
                    top: clonedObj.top + 10,
                    evented: true,
                    cornerColor: 'gray',
                    cornerSize: 10,
                    transparentCorners: false,
                });
                canvas.add(clonedObj);
                canvas.requestRenderAll();
            });
        }
    });
    /* --------------------- end functions --------------------- */

    /* --------------------- start variables --------------------- */

    let ctrl = $(this).find('.ctrl');
    let canvasClicked = false;
    let rectSelected = false;
    let rectRadius = false;
    let colorsList = $(this).find('.colors-list');
    let sq_cr = $(this).find('.square-circle .far');
    let prompt = $(this).find('.prompt');
    let nameTable = $(this).find('.nameTable');
    let rect_text = 'T';
    let rect_price =  '00';
    let rect_active ='UnActive';
    parseFloat(rect_price);
    let idCanvas = $(this).find('.container-canvas').find('canvas').attr('id');
    let canvas = new fabric.Canvas(idCanvas, {
        width: window.innerWidth,
        height: window.innerHeight,
        backgroundColor : '#f0f0fa',

    });

    //  ctx = canvas.getContext("2d");
    // document.fonts.ready.then(_ => {
    //   ctx.font = '600 48px "Font Awesome 5 Free"';
    //   ctx.fillStyle = "black";
    //   setTimeout(_ => ctx.fillText("\uF200", 45, 45), 200);
    // });
    document.getElementById(idCanvas).fabric = canvas;
    let svgState = {};
    /* --------------------- end variables --------------------- */

    /* --------------------- start render --------------------- */
    setPanEvents(canvas);

    // $( window ).bind('beforeunload', function() {
    //     let json = canvas.toJSON();
    //     localStorage.setItem('design', JSON.stringify(json));
    // });

    // $(document).ready(function () {
    //     if (localStorage.getItem("design") !== null) {

    //         const json = localStorage.getItem("design");
    //         canvas.loadFromJSON($.parseJSON(json), canvas.renderAll.bind(canvas))
    //         console.log('json' + JSON.stringify(canvas.toJSON()) + idCanvas);

    //         // console.log('here' + canvas.toSVG());

    //     }
    // });

    /* --------------------- end render --------------------- */
});

