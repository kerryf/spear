package spear.security;

import io.javalin.security.RouteRole;

public final class EndpointRole implements RouteRole
{
    private final String role;

    private EndpointRole(String role)
    {
        this.role = role;
    }

    public static EndpointRole of(String role)
    {
        return new EndpointRole(role);
    }

    public String getRole()
    {
        return role;
    }

    @Override
    public String toString()
    {
        return "EndpointRole {role=" + role + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        EndpointRole that = (EndpointRole) o;

        return role.equals(that.role);
    }

    @Override
    public int hashCode()
    {
        return role.hashCode();
    }
}
