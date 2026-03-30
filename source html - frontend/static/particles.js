window.onmousemove = () => {
    var particles_index = document.getElementsByClassName("particles")[0];
    var x = event.clientX;
    var y = event.clientY; 
    var mid_y = window.innerHeight;
    var mid_x = window.innerWidth;
    var movement = 15;
    var testx = (x < mid_x ? -((movement-0)/(mid_x-0)*(x-mid_x)+0) : ((movement-0)/(mid_x-0)*(x-mid_x)+0));
    var testy = (y > mid_y ? -((movement-0)/(mid_y-0)*(y-mid_y)+0) : ((movement-0)/(mid_y-0)*(y-mid_y)+0));
    
    particles_index.style.bottom = testy;
    particles_index.style.left = testx;
}