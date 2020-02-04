package com.example.segurancabelica.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.model.Usuario;

import java.util.List;


public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuariosViewHolder> {

    private List<Usuario> listaUsuarios;

    public ListaUsuariosAdapter(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_usuarios, parent, false);

        return new UsuariosViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {

        Usuario usuario = listaUsuarios.get(position);

        holder.nome.setText(usuario.getNome());
        holder.posto.setText(usuario.getPosto());
        String cartão = "Cartão: " + usuario.getCodigoCartao();
        holder.codigo.setText(cartão);
        holder.nivel.setText(usuario.getPermissao());
        holder.email.setText(usuario.getEmail());
    }

    @Override
    public int getItemCount() {
        return this.listaUsuarios.size();
    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {

        private TextView nome;
        private TextView posto;
        private TextView codigo;
        private TextView nivel;
        private TextView email;


        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            posto = itemView.findViewById(R.id.textPosto);
            codigo = itemView.findViewById(R.id.textCodigo);
            nivel = itemView.findViewById(R.id.textNivel);
            email = itemView.findViewById(R.id.textEmail);

        }
    }

}
