

new Vue({
    el: "#root",
    data: {
        message: "Hello World!!",

    }
})


var arr = new Vue({
    el: "#array",
    data: {
       newName:'',
       namesArray: ['Joe', 'Mary', 'Jane', 'Jack']

    },
    methods: {
       onClick() {
           this.namesArray.push(this.newName)
           this.newName = ''
       }
    }
})