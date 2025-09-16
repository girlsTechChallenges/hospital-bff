package com.fiap.hospital.infra.domain;

import java.util.List;

public class RoleUser {
    private int id;
    private String nome;
    private List<role> scope;

    public static class role{
        private String roleName;
    }
}
