const ProjectPage = {
  template: ` 

  <div> 
  <p style="display:inline">Project:</p>
      <div class="two-split"> 
     <div>
      
        <h1>{{ project.name }}</h1> 
        <h3>{{ project.status }} </h3>
        </div>
        
        <div>
        <h4>Description</h4>
        <p>{{ project.description }}</p>
        </div>
      </div> 
      <div  class="two-split">  
      <table>  
      <div style="display:flex;flex-direction:row;" >
      <h4 style="margin-right:40px">Members working in this project</h4> 

      <form  style="display:flex;flex-direction:row;height:50px;width:100%"> 
        <label>Add member to project</label>
        <select v-model="memberToAdd" name="addProjectMember"> 
          <option v-for="member in projectNotMembers" v-bind:value="member.id">{{ member.name }}</option>
        </select> 
        <button @click="addProjectMember">Add member</button>
      </form>
      </div>
        <tr>
          <th>Member Id</th>
          <th>Member name</th> 
          <th>Remove member</th>
        </tr> 
        <tr v-for="projectMember in projectMembers"> 
          <td>{{projectMember.memberId}}</td>
          <td>{{projectMember.memberName}}</td> 
          <td><button @click="removeMember(projectMember.memberId)">Remove</button></td>
        </tr>
      </table>
       <form @submit="editProject"> 
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
            <label for="status">Status</label>
          </div>
          <div class="col-75">
            <input type="text" id="status" name="status" placeholder="Project status" v-model="editForm.status" required> <br> <br>
          </div>
        </div> 
        <div class="row">
          <div class="col-25">
            <label for="description">Description</label>
          </div>
          <div class="col-75">
            <textarea v-model="editForm.description" name="description" id="description" cols="30" rows="10" ></textarea><br>
          </div>
        </div>
        <div class="row">
          <input type="submit" value="Save"> 
        </div>
       </form> 
       <br>
         <button type="button" class="deleteButton" @click="deleteProject()">Delete Project</button>         
        </div>  
     </div> 
                `,

  data() {
    return {
      project: {},
      projectMembers: {},
      projectNotMembers: {}, 
      memberToAdd: "",
      editForm: {
        name: "",
        description: "",
        status: ""
      }
    };
  },
  methods: { 
        addProjectMember() { 
      if(this.memberToAdd != "") { 
      let body = `memberId=${this.memberToAdd}&projectId=${this.$route.params.id}`;
      body = encodeURI(body);
      axios({
        method: "put",
        url: "/api/projectmembers",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        }); 
      this.memberToAdd = "";
      this.projectNotMembers = {};
      this.getProject();
      }
    }, 
     removeMember(memberId) { 
      let body = `memberId=${this.memberToAdd}&projectId=${this.$route.params.id}`;
      body = encodeURI(body);
      axios({
        method: "delete",
        url: "/api/projectmembers",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        }); 

      this.getProject();
      
    },
    deleteProject() {
      let body = `id=${this.$route.params.id}`;
      body = encodeURI(body);
      axios({
        method: "delete",
        url: "/api/projects",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        });
      this.$router.push("/projects");
    },
    editProject() {
      let body = `id=${this.$route.params.id}&name=${this.editForm.name}&status=${this.editForm.status}&description=${this.editForm.description}`;
      body = encodeURI(body);
      axios({
        method: "post",
        url: "/api/projects",
        data: body
      })
        .then(response => console.log(response))
        .catch(function(error) {
          console.log(error);
        });
      this.getProject();
    },
    getProject() {
      axios({
        method: "get",
        url: "/api/projects?id=" + this.$route.params.id
      })
        .then(response => (this.project = response.data))
        .catch(function(error) {
          console.log(error);
        });
      axios({
        method: "get",
        url: "/api/projectmembers?projectId=" + this.$route.params.id
      })
        .then(response => (this.projectMembers = response.data))
        .catch(function(error) {
          console.log(error);
        }); 
      axios({
        method: "get",
        url: "/api/members?projectid=" + this.$route.params.id
      })
        .then(response => (this.projectNotMembers = response.data))
        .catch(function(error) {
          console.log(error);
        });
    }
  },
  mounted() {
    this.getProject();
  }
};

export default ProjectPage;
