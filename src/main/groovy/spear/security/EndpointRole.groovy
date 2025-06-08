package spear.security;

import io.javalin.security.RouteRole;

final class EndpointRole implements RouteRole
{
    private final String role

    private EndpointRole(String role)
    {
        this.role = role
    }

    static EndpointRole of(String role)
    {
        return new EndpointRole(role)
    }

    String getRole()
    {
        return role
    }

    @Override
    String toString()
    {
        return "EndpointRole {role=${role}}"
    }

    @Override
    boolean equals(Object o)
    {
        if (this.is(o))
        {
            return true
        }

        if (!o || getClass() != o.getClass())
        {
            return false
        }

        EndpointRole that = (EndpointRole) o

        return role == that.role
    }

    @Override
    int hashCode()
    {
        return role.hashCode()
    }
}
