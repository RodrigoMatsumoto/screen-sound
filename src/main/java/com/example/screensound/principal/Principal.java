package com.example.screensound.principal;

import com.example.screensound.model.Artista;
import com.example.screensound.model.Musica;
import com.example.screensound.model.TipoArtista;
import com.example.screensound.repository.ArtistaRepository;
import com.example.screensound.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

  private final ArtistaRepository artistaRepository;
  private final Scanner scanner = new Scanner(System.in);

  public Principal(ArtistaRepository artistaRepository) {
    this.artistaRepository = artistaRepository;
  }
  public void exibeMenu() {
    var opcao = -1;

    while (opcao != 0) {

      var menu = """
          *** Screen Sound Músicas***
          
          1 - Cadastrar artistas
          2 - Cadastrar músicas
          3 - Listar músicas
          4 - Buscar músicas por artistas
          5 - Pesquisar dados sobre um artista
          
          0 - Sair
          """;

      System.out.println(menu);
      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          cadastrarArtista();
          break;
        case 2:
          cadastrarMusica();
          break;
        case 3:
          listarMusicas();
          break;
        case 4:
          buscarMusicasPorArtista();
          break;
        case 5:
          pesquisarDadosDoArtista();
          break;
        case 0:
          System.out.println("Encerrando a aplicação!");
          break;
        default:
          System.out.println("Opção inválida");
      }
    }
  }

  private void cadastrarArtista() {
    var cadastrarNovo = "s";

    while (cadastrarNovo.equalsIgnoreCase("s")) {
      System.out.println("Informe o nome desse artista:");
      var nome = scanner.nextLine();

      System.out.println("Informe o tipo desse artista: (solo, dupla ou banda)");
      var tipo = scanner.nextLine();

      TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
      Artista artista = new Artista(nome, tipoArtista);

      artistaRepository.save(artista);

      System.out.println("Cadastrar novo artista? (s/n)");
      cadastrarNovo = scanner.nextLine();
    }
  }

  private void cadastrarMusica() {
    System.out.println("Cadastrar música de qual artista?");
    var nome = scanner.nextLine();

    Optional<Artista> artista = artistaRepository.findByNomeContainingIgnoreCase(nome);

    if (artista.isPresent()) {
      System.out.println("Informe o título da música: ");
      var nomeMusica = scanner.nextLine();

      Musica musica = new Musica(nomeMusica);
      musica.setArtista(artista.get());
      artista.get().getMusicas().add(musica);

      artistaRepository.save(artista.get());
    } else {
      System.out.println("Artista não encontrado");
    }
  }

  private void listarMusicas() {
    artistaRepository.findAll().forEach(m -> m.getMusicas().forEach(System.out::println));
  }

  private void buscarMusicasPorArtista() {
    System.out.println("Buscar música de qual artista? ");
    var nome = scanner.nextLine();

    List<Musica> musicas = artistaRepository.buscarMusicaPorArtista(nome);

    musicas.forEach(System.out::println);
  }

  private void pesquisarDadosDoArtista() {
    System.out.println("Pesquisar dados de qual artista? ");
    var nome = scanner.nextLine();

    var resposta = ConsultaChatGPT.obterInformacao(nome);
    System.out.println(resposta.trim());
  }
}