package no.kristiania.ProjectManager;

import java.util.Objects;

public class ProjectMember {
    private int projectId;
    private int memberId;
    private String projectName;
    private String projectStatus;
    private String memberName;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return projectId == that.projectId &&
                memberId == that.memberId &&
                Objects.equals(projectName, that.projectName) &&
                Objects.equals(projectStatus, that.projectStatus) &&
                Objects.equals(memberName, that.memberName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, memberId, projectName, projectStatus, memberName);
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "projectId=" + projectId +
                ", memberId=" + memberId +
                ", projectName='" + projectName + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
