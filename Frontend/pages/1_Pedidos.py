import streamlit as st
import requests


st.set_page_config(page_title="Pedidos", page_icon="🍔")
st.header("Pedidos")

# Initialize session state for order items
if 'order_items' not in st.session_state:
    st.session_state.order_items = []

# Input for table number
table_number = st.number_input("Número da Mesa", min_value=1, step=1)

# Form to add items to the order
item_type = st.selectbox("Selecione o tipo de item", ["Bebida", "Comida", "Sobremesa"], key='item_type')

with st.form(key='item_form'):
    if item_type == "Bebida":
        tipo = st.text_input("Tipo")
        sabor = st.text_input("Sabor")
        observacao = st.text_input("Observacao")
        item = {"tipo": tipo, "sabor": sabor, "observacao": observacao, "mesa": table_number}
    
    elif item_type == "Comida":
        menu = st.text_input("Menu")
        tipo = st.text_input("Tipo")
        observacao = st.text_input("Observacao")
        item = {"menu": menu, "tipo": tipo, "observacao": observacao, "mesa": table_number}
    
    elif item_type == "Sobremesa": 
        tipo = st.text_input("Tipo")
        sabor = st.text_input("Sabor")
        observacao = st.text_input("Observacao")
        item = {"tipo": tipo, "sabor": sabor, "observacao": observacao, "mesa": table_number}
    
    add_item = st.form_submit_button("Adicionar item")
    
    if add_item:
        st.session_state.order_items.append((item_type, item))
        st.success("Item adicionado!")

# Display the current order items
st.subheader("Carrinho")
for i, (item_type, item) in enumerate(st.session_state.order_items):
    st.write(f"Item {i+1}: {item_type} - {item}")

# Submit the complete order
if st.button("Enviar pedido"):
    if(len(st.session_state.order_items) == 0):
        st.error("Nenhum item adicionado ao pedido")
        st.stop()
    for item_type, item in st.session_state.order_items:
        if item_type == 'Bebida':
            response = requests.post("http://localhost:8080/camel/orders/drink", json=item)
        elif item_type == 'Comida':
            response = requests.post("http://localhost:8080/camel/orders/food", json=item)
        elif item_type == 'Sobremesa':
            response = requests.post("http://localhost:8080/camel/orders/dessert", json=item)
        st.write(response.text)
    
    # Clear the order items after submission
    st.session_state.order_items = []
    st.success("Pedido enviado com sucesso")