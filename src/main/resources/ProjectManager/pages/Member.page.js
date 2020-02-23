const MemberPage = {
  template: `
                  <div> 
                    <div> 
                    <p>User:</p>
                      <h1>{{ user.name }}</h1> 
                      <h3>{{ user.email }} </h3>
                    </div> 
                    <div  class="two-split">  

                    <table>  
                    <h4>Projects this user is in</h4>
                      <tr>
                        <th>Prosjekt Id</th>
                        <th>Prosjekt</th>
                        <th>ProsjektStatus</th>
                      </tr> 
                      <tr v-for="userProject in userProjects"> 
                        <td>{{userProject.projectId}}</td>
                        <td>{{userProject.projectName}}</td>
                        <td>{{userProject.projectStatus}}</td>
                      </tr>
                    </table>

                    <form @submit="editMember"> 
                    <h4>Edit user</h4>
                        <div class="row">
                          <div class="col-25">
                            <label for="name">Name</label>
                          </div>
                          <div class="col-75">
                            <input type="text" id="name" name="name" placeholder="Member name" v-model="editForm.name" required>
                          </div>
                      </div>
                      <div class="row">
                        <div class="col-25">
                          <label for="email">Email</label>
                        </div>
                        <div class="col-75">
                          <input type="email" id="email" name="email" placeholder="Member Email" v-model="editForm.email" required> <br> <br>
                        </div>
                      </div>
                      <div class="row">
                        <input type="submit" value="Save"> 
                      </div>
                    </form> 
                    <br>
                      <button type="button" class="deleteButton" @click="deleteMember()">Slett bruker</button>         
                      </div>  
                  </div>
                  `,

  data() {
    return {
      user: {},
      userProjects: "",
      editForm: {
        name: "",
        email: ""
      }
    };
  },
  methods: {
    deleteMember() {
      let body = `id=${this.$route.params.id}`;
      body = encodeURI(body);
      axios({
        method: "delete",
        url: "/api/members",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        });
      this.$router.push("/members");
    },
    editMember() {
      if (this.editForm.name.lenght < 1) {
        this.editForm.name = this.user.name;
      }

      if (this.editForm.email.lenght < 1) {
        this.editForm.email = this.user.email;
      }

      let body = `id=${this.$route.params.id}&name=${this.editForm.name}&email=${this.editForm.email}`;
      body = encodeURI(body);
      axios({
        method: "post",
        url: "/api/members",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        });
      this.getMember();
    },
    getMember() {
      var userData;
      axios({
        method: "get",
        url: "/api/members?id=" + this.$route.params.id
      })
        .then(response => (this.user = response.data))
        .catch(function(error) {
          console.log(error);
        });
      axios({
        method: "get",
        url: "/api/projectmembers?memberId=" + this.$route.params.id
      })
        .then(response => (this.userProjects = response.data))
        .catch(function(error) {
          console.log(error);
        });
    }
  },
  mounted() {
    this.getMember();
  }
};

export default MemberPage;
