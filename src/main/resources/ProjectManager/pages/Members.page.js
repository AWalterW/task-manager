Vue.component("new-member-form", {
  data() {
    return {
      user: {},
      newMemberForm: {
        name: "",
        email: ""
      }
    };
  },
  template: `
 <form @submit="addMember"> 
      <h4>New User</h4>
        <div class="row">
          <div class="col-25">
            <label for="name">Name</label>
          </div>
          <div class="col-75">
            <input type="text" id="name" name="name" placeholder="Member name" v-model="newMemberForm.name" required>
          </div>
        </div>
        <div class="row">
          <div class="col-25">
            <label for="email">Email</label>
          </div>
          <div class="col-75">
            <input type="email" id="email" name="email" placeholder="Member Email" v-model="newMemberForm.email" required> <br> <br>
          </div>
        </div>
        <div class="row">
          <input type="submit" value="Add member">
        </div>
      </form>
  `,
  methods: {
    addMember(e) { 
      e.preventDefault();  
      let body = `name=${this.newMemberForm.name}&email=${this.newMemberForm.email}`;
      body = encodeURI(body);
      axios({
        method: "put",
        url: "/api/members",
        data: body
      })
        .then(function(response) {
          currentObj.output = response.data;
        })
        .catch(function(error) {
          currentObj.output = error;
        });
      this.newMemberForm.name = "";
      this.newMemberForm.email = ""; 
      this.$emit('updated', 'updated')
    }
  }
});

const MemberPage = {
  data() {
    return {
      members: ""
    };
  },
  template: `  
  <div>
  <div class="two-split">
<table> 
      <h3>Members:</h3>
      <tr> 
        <th>Id</th>
        <th>Name</th>
        <th>Email</th> 
      </tr>
      <tr v-for="member in members" @click="openUser(member.id)" style="cursor: pointer">
        <td>{{member.id}}</td>
        <td >{{member.name}}</td>
        <td>{{member.email}}</td>
      </tr>
    </table> 

    <new-member-form @updated="getData()"></new-member-form>
    </div>  

  </div>
  `,
  methods: {
    getData: function() {
      axios
        .get("http://localhost:8080/api/members")
        .then(response => (this.members = response.data));
    },
    openUser(id) {
      this.$router.push("/member/" + id);
    }
  },
  mounted() {
    this.getData();
  }
};

export default MemberPage;
