import pytest
from straysafe.app import app

@pytest.fixture
def client():
    app.config.update({
        "TESTING": True,
    })

    with app.test_client() as client:
        yield client

def test_hello_world(client):
    """Test the hello world route."""
    response = client.get('/api/hello')
    assert response.status_code == 200
    assert response.json == {'message': 'Hello, World!'}

def test_echo(client):
    """Test the echo route."""
    sent_data = {'key': 'value'}
    response = client.post('/api/echo', json=sent_data)
    assert response.status_code == 200
    assert response.json == {'you sent': sent_data}
