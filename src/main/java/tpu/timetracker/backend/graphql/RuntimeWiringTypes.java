package tpu.timetracker.backend.graphql;

import graphql.schema.idl.TypeRuntimeWiring;
import tpu.timetracker.backend.model.AbstractEntity;
import tpu.timetracker.backend.model.Project;
import tpu.timetracker.backend.model.Task;
import tpu.timetracker.backend.model.TimeEntry;
import tpu.timetracker.backend.model.User;
import tpu.timetracker.backend.model.Workspace;

import java.util.function.UnaryOperator;

class RuntimeWiringTypes {

  private static class CustomTypeRuntimeWiring {
    private CustomTypeRuntimeWiring() {
    }

    static TypeRuntimeWiring newTypeWiring(String typeName, UnaryOperator<TypeRuntimeWiring.Builder> builderFunction) {
      return builderFunction.apply(new TypeRuntimeWiring.Builder()
          .typeName(typeName)
          .dataFetcher("id", env -> env.<AbstractEntity>getSource().getId()))
          .dataFetcher("crdate", env -> env.<AbstractEntity>getSource().getCreationDate())
          .build();
    }
  }

  static TypeRuntimeWiring workspaceTypeWiring = CustomTypeRuntimeWiring.newTypeWiring(
      "Workspace", builder ->
          builder
              .dataFetcher("name", env -> env.<Workspace>getSource().getName())
              .dataFetcher("ownerId", env -> env.<Workspace>getSource().getOwnerId()));

  static TypeRuntimeWiring userTypeWiring = CustomTypeRuntimeWiring.newTypeWiring(
      "User", builder ->
          builder
              .dataFetcher("username", env -> env.<User>getSource().getUsername())
              .dataFetcher("name", env -> env.<User>getSource().getName())
              .dataFetcher("email", env -> env.<User>getSource().getEmail()));

  static TypeRuntimeWiring projectTypeWiring = CustomTypeRuntimeWiring.newTypeWiring(
      "Project", builder ->
          builder
              .dataFetcher("name", env -> env.<Project>getSource().getName())
              .dataFetcher("workspace", env -> env.<Project>getSource().getWorkspace()));

  static TypeRuntimeWiring taskTypeWiring = CustomTypeRuntimeWiring.newTypeWiring(
      "Task", builder ->
          builder
              .dataFetcher("description", env -> env.<Task>getSource().getDescription())
              .dataFetcher("name", env -> env.<Task>getSource().getName())
              .dataFetcher("project", env -> env.<Task>getSource().getProject())
              .dataFetcher("timeEntry", env -> env.<Task>getSource().getTimeEntry()));

  static TypeRuntimeWiring timeEntryTypeWiring = CustomTypeRuntimeWiring.newTypeWiring(
      "TimeEntry", builder ->
          builder
              .dataFetcher("duration", env -> env.<TimeEntry>getSource().getDuration())
              .dataFetcher("endDate", env -> env.<TimeEntry>getSource().getEndDate())
              .dataFetcher("startDate", env -> env.<TimeEntry>getSource().getStartDate())
              .dataFetcher("task", env -> env.<TimeEntry>getSource().getTask()));

  static TypeRuntimeWiring queryTypeWiring = TypeRuntimeWiring.newTypeWiring(
      "Query", builder ->
          builder
              .dataFetcher("workspace", Fetchers.workspaceDataFetcher)
              .dataFetcher("workspaces", Fetchers.workspaceDataFetcher)
              .dataFetcher("user", Fetchers.userDataFetcher));
}