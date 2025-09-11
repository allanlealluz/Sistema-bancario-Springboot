package com.example.conta_bancaria.interfaceui.controller;

import com.example.conta_bancaria.application.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aluno")
public class ClienteController {
    @Autowired
    ClienteService clienteService;
}
