package com.totvs.contas_pagar.adapter.infra.config;

import com.totvs.contas_pagar.domain.ports.ContaRepositoryPort;
import com.totvs.contas_pagar.domain.ports.ContaServicePort;
import com.totvs.contas_pagar.domain.services.ContaService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ContaServicePort contaServicePort(ContaRepositoryPort contaRepositoryPort) {
        return new ContaService(contaRepositoryPort);
    }
}
