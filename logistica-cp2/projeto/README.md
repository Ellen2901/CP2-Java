**FIAP · 2ESPZ · 2026 · Domain Driven Design — CP2**

- Camila de Mendonça Silva - RM: 565491 
- Luana Luo - RM: 562271
- Julia Silva Santos - RM: 564670
- Sidyellen Souza - RM: 566408

---

## Como executar no IntelliJ IDEA

1. Abra o IntelliJ → **File > Open** → selecione a pasta `logistica-cp2/`
2. Clique com botão direito na pasta `src/` → **Mark Directory as > Sources Root**
3. Vá em **Run > Run 'Main'** (ou use `Shift+F10`)

> **Requisitos:** Java 17+ com Swing (JDK completo, não headless)

---

## Estrutura do Projeto

```
src/
├── Main.java                        ← Ponto de entrada (SwingUtilities.invokeLater)
├── domain/
│   ├── Entregavel.java              ← Interface (comportamento de entrega)
│   ├── StatusEntrega.java           ← Enum de status
│   ├── Entregador.java              ← Classe abstrata
│   ├── EntregadorMoto.java          ← Subclasse concreta
│   ├── EntregadorBicicleta.java     ← Subclasse concreta
│   ├── EntregadorCarro.java         ← Subclasse concreta
│   └── Entrega.java                 ← Entidade de entrega/pedido
├── service/
│   └── SistemaLogistica.java        ← Regras de negócio e gerenciamento
└── ui/
    ├── Theme.java                   ← Constantes visuais (cores, fontes)
    ├── UiUtil.java                  ← Componentes Swing reutilizáveis
    ├── MainFrame.java               ← Janela principal com CardLayout
    ├── DashboardPanel.java          ← Painel de estatísticas
    ├── EntregadoresPanel.java       ← CRUD de entregadores
    ├── EntregasPanel.java           ← CRUD de entregas
    └── OperacoesPanel.java          ← Atribuição/finalização/cancelamento
```

---
